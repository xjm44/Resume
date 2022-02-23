#include "hashmap.h"
#include "qalloc.h"

#include <pthread.h>
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

		pthread_barrier_init(&map->lock,NULL,2); // because the default of test has 2 threads
	  return;

}

void hashmap_insert(struct hashmap* map,
		void* key,
		void* value) {

			register node* temp = NULL;
			register node* prev = NULL;
			register node* node1 = mem_alloc(sizeof(node));

			if(node1 ==NULL){
				// printf("failed allocate mem for node\n" );
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
        // pthread_barrier_wait(&map->lock);
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
            // pthread_barrier_wait(&map->lock);
						return;
					}
					prev = temp;
					temp = temp->next;
				}
				prev->next = node1;
			}
      // pthread_barrier_wait(&map->lock);
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

			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("key found!\n" );
        pthread_barrier_wait(&map->lock);
				return temp->value;
			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("key found!\n" );
            pthread_barrier_wait(&map->lock);
						return temp->value;
					}
					temp = temp->next;
				}
			}
			// printf("key not found\n" );
      pthread_barrier_wait(&map->lock);
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

			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("entry found!\n" );
				pair* p = mem_alloc(sizeof(pair));
				p->key = temp->key;
				p->value  =temp->value;
        pthread_barrier_wait(&map->lock);
				return p;

			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("entry found!\n" );
						pair* p = mem_alloc(sizeof(pair));
						p->key = temp->key;
						p->value  =temp->value;
            pthread_barrier_wait(&map->lock);
						return p;

					}
					temp = temp->next;
				}
			}
      pthread_barrier_wait(&map->lock);
			return NULL;

}

void* hashmap_remove_value(struct hashmap* map,
		void* key) {


			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_value\n" );
				return NULL;
			}


			register node* prev = NULL;

			// if first index matched
			if(map->cmp(temp->key,key)==0){
				void * value = temp->value;
				map->array[index]  =temp->next;
				map->size--;
        pthread_barrier_wait(&map->lock);
				return value;
			}else{

				prev = temp;
				temp = temp->next;
				while(temp!=NULL){

					if(map->cmp(temp->key,key)==0){

						void * value = temp->value;
						prev->next = temp->next;
						map->size--;
            pthread_barrier_wait(&map->lock);
						return value;
					}
					prev = temp;
					temp = temp->next;
				}
			}
			// printf("not found for remove_value\n" );
      pthread_barrier_wait(&map->lock);
			return NULL;
}

void* hashmap_remove_entry(struct hashmap* map,
		void* key) {

			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
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
        pthread_barrier_wait(&map->lock);
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
            pthread_barrier_wait(&map->lock);
						return p;
					}
					prev = temp;
					temp = temp->next;
				}
			}
      pthread_barrier_wait(&map->lock);
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
	pthread_barrier_destroy(&map->lock);
	return;
}
