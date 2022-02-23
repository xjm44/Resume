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





void lock_init(lockk* l){
  l->lock = UNLOCKED;
}

int to_lock(lockk* l){
  if(l->lock != LOCKED && l->lock != UNLOCKED){
    return 99;
  }
  int temp = UNLOCKED;
  while(!__atomic_compare_exchange_n(&l->lock, &temp, LOCKED, false, __ATOMIC_SEQ_CST, __ATOMIC_SEQ_CST)){
    temp = UNLOCKED;
  }
   return 0;
}

int to_unlock(lockk* l){
  if(l->lock != LOCKED && l->lock != UNLOCKED){
    return 99;
  }
  __atomic_store_n(&l->lock, UNLOCKED, __ATOMIC_SEQ_CST);
  return 0;
}

void done_locking(lockk* l){
  l->lock = 2;
}

lockk l;

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


    lock_init(&l);
    map->lock = &l;

	  return;

}

void hashmap_insert(struct hashmap* map,
		void* key,
		void* value) {

      to_lock(map->lock);

			register node* temp = NULL;
			register node* prev = NULL;
			register node* node1 = mem_alloc(sizeof(node));

			if(node1 ==NULL){
				// printf("failed allocate mem for node\n" );
        to_unlock(map->lock) ;
				return;
			}

			node1->key = (key);
			node1->value = (value);
			node1->next = NULL;

			// get index for hash
			int index = map->hash(map,key)% map->arraysize;
			// printf("%d\n",index );

			// insert node if index is free
			if(map->array[index] ==NULL){
				map->array[index] = node1;
				map->size++;
				// printf("**\n" );
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
						// map->size++;
						// printf("duplicated key, value replaced!\n" );
						to_unlock(map->lock) ;
						return;
					}
					prev = temp;
					temp = temp->next;
				}
				prev->next = node1;
			}
			// printf("inserted!\n" );
			to_unlock(map->lock) ;
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

			to_lock(map->lock);
			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("key found!\n" );
				to_unlock(map->lock) ;
				return temp->value;
			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("key found!\n" );
						to_unlock(map->lock) ;
						return temp->value;
					}
					temp = temp->next;
				}
			}
			// printf("key not found\n" );
			to_unlock(map->lock) ;
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

			to_lock(map->lock);

			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("entry found!\n" );
				pair* p = mem_alloc(sizeof(pair));
				p->key = temp->key;
				p->value  =temp->value;
				to_unlock(map->lock) ;
				return p;


			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("entry found!\n" );
						pair* p = mem_alloc(sizeof(pair));
						p->key = temp->key;
						p->value  =temp->value;
						to_unlock(map->lock) ;
						return p;

					}
					temp = temp->next;
				}
			}
			// printf("entry not found\n" );
			to_unlock(map->lock) ;
			return NULL;

}

void* hashmap_remove_value(struct hashmap* map,
		void* key) {

      to_lock(map->lock);
			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_value\n" );
        to_unlock(map->lock) ;
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
				to_unlock(map->lock) ;
				return value;
			}else{
				prev = temp;
				temp = temp->next;
				while(temp!=NULL){

					if(map->cmp(temp->key,key)==0){

						void * value = temp->value;

						prev->next = temp->next;

						map->size--;
						to_unlock(map->lock) ;
						return value;
					}
					prev = temp;
					temp = temp->next;
				}
			}

			// printf("not found for remove_value\n" );
			to_unlock(map->lock) ;
			return NULL;
}

void* hashmap_remove_entry(struct hashmap* map,
		void* key) {

      to_lock(map->lock);
			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_entry\n" );
        to_unlock(map->lock) ;
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
				to_unlock(map->lock) ;
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
						to_unlock(map->lock) ;
						return p;
					}
					prev = temp;
					temp = temp->next;
				}
			}
			// printf("not found for remove_entry\n" );
			to_unlock(map->lock) ;
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
	done_locking(map->lock);
	return;
}
