#include "hashmap.h"
#include "qalloc.h"

#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <stddef.h>
#include <time.h>
#include <stdint.h>





void hashmap_init(struct hashmap* map,
		size_t(*hash)(struct hashmap*,void*),
		void(*key_del)(void*),
		void(*val_del)(void*),
		int(*cmp)(void*, void*),
		size_t size,
		size_t objsize) {

		map->key_del = key_del;
	  map->val_del = val_del;
	  map->cmp = cmp;
		map->hash = hash;

	  map->array = mem_alloc(sizeof(node*)*size);

		for(int i=0;i<size;i++){
			map->array[i] = NULL;
		}

		map->size = size;
		map->objsize= objsize;
		map->arraysize = size;

		sem_init(&map->lock,0,1);
	  return;

}

void hashmap_insert(struct hashmap* map,
		void* key,
		void* value) {

			sem_wait(&map->lock);

			register node* temp = NULL;
			register node* prev = NULL;
			register node* node1 = mem_alloc(sizeof(node));

			if(node1 ==NULL){
				// printf("failed allocate mem for node\n" );
				sem_post(&map->lock) ;
				return;
			}

			node1->key = (key);
			node1->value = (value);
			node1->next = NULL;
			// get index for hash
			int index = map->hash(map,key)% map->arraysize;

			// insert node if index is free
			if(map->array[index] ==NULL){
				map->array[index] = node1;
				map->size++;
			}else{
				// otherwise insert at later position where it is null

				temp = map->array[index];
				prev = temp;
				while(temp!=NULL){
					// duplicated key, replace value
					if(map->cmp(temp->key, node1->key)==0){
						map->val_del(temp->value);
						mem_free(node1);
						temp->value = value;
						sem_post(&map->lock) ;
						return;
					}
					prev = temp;
					temp = temp->next;
				}
				prev->next = node1;
			}
			// printf("inserted!\n" );
			sem_post(&map->lock) ;
			return;

}

void* hashmap_get(struct hashmap* map,
		void* key) {

			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("key not found\n" );
				return NULL;
			}

			sem_wait(&map->lock);
			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("key found!\n" );
				sem_post(&map->lock) ;
				return temp->value;
			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("key found!\n" );
						sem_post(&map->lock) ;
						return temp->value;
					}
					temp = temp->next;
				}
			}
			// printf("key not found\n" );
			sem_post(&map->lock) ;
			return NULL;
}

void* hashmap_get_entry(struct hashmap* map,
		void* key) {

			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("entry not found\n" );
				return NULL;
			}

			sem_wait(&map->lock);

			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("entry found!\n" );
				pair* p = mem_alloc(sizeof(pair));
				p->key = temp->key;
				p->value  =temp->value;
				sem_post(&map->lock) ;
				return p;

			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("entry found!\n" );
						pair* p = mem_alloc(sizeof(pair));
						p->key = temp->key;
						p->value  =temp->value;
						sem_post(&map->lock) ;
						return p;
					}
					temp = temp->next;
				}
			}
			// printf("entry not found\n" );
			sem_post(&map->lock) ;
			return NULL;

}

void* hashmap_remove_value(struct hashmap* map,
		void* key) {

			sem_wait(&map->lock);
			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_value\n" );
				sem_post(&map->lock) ;
				return NULL;
			}

			register node* prev = NULL;
			// if first index matched
			if(map->cmp(temp->key,key)==0){
				void * value = temp->value;
				map->array[index]  =temp->next;
				// map->key_del(temp->key);
				// mem_free(temp);
				map->size--;
				sem_post(&map->lock) ;
				return value;
			}else{
				prev = temp;
				temp = temp->next;
				while(temp!=NULL){

					if(map->cmp(temp->key,key)==0){
						void * value = temp->value;
						prev->next = temp->next;
						map->size--;
						sem_post(&map->lock) ;
						return value;
					}
					prev = temp;
					temp = temp->next;
				}
			}

			// printf("not found for remove_value\n" );
			sem_post(&map->lock) ;
			return NULL;
}

void* hashmap_remove_entry(struct hashmap* map,
		void* key) {

			sem_wait(&map->lock);

			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_entry\n" );
				sem_post(&map->lock) ;
				return NULL;
			}

			register node* prev = NULL;
			// if first index matched
			if(map->cmp(temp->key,key)==0){

				pair* p = mem_alloc(sizeof(pair));
				p->key = temp->key;
				p->value  =temp->value;
				map->array[index]=temp->next;
				map->size--;
				sem_post(&map->lock) ;
				return p;

			}else{
				prev = temp;
				temp = temp->next;
				while(temp!=NULL){
					if(map->cmp(temp->key,key)==0){

						pair* p = mem_alloc(sizeof(pair));

						p->key = temp->key;
						p->value  =temp->value;
						prev->next = temp -> next;
						map->size--;
						sem_post(&map->lock) ;
						return p;
					}
					prev = temp;
					temp = temp->next;
				}
			}
			// printf("not found for remove_entry\n" );
			sem_post(&map->lock) ;
			return NULL;

}

size_t hashmap_size(struct hashmap* map) {
	return map->size;
}

void hashmap_destroy(struct hashmap* map) {

	for(int i = 0; i <map->arraysize; i++){
		register node* temp = map->array[i];
		register node* prev = temp;

		while(temp!=NULL){
			prev = temp;
			temp = temp->next;
			map->key_del(prev->key);
			map->val_del(prev->value);
			mem_free(prev);
		}
	}
	mem_free(map->array);
	// printf("freed!\n" );
	sem_destroy(&map->lock);
	return;
}
