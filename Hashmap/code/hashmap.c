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

		pthread_rwlock_init(&map->lock,NULL);
	  return;

}

void hashmap_insert(struct hashmap* map,
		void* key,
		void* value) {

			pthread_rwlock_wrlock(&map->lock);

			register node* temp = NULL;
			register node* prev = NULL;
			register node* node1 = mem_alloc(sizeof(node));

			if(node1 ==NULL){
				// printf("failed allocate mem for node\n" );
				mem_free(node1);
				pthread_rwlock_unlock(&map->lock) ;
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
						pthread_rwlock_unlock(&map->lock) ;
						return;
					}
					prev = temp;
					temp = temp->next;
				}
				prev->next = node1;
				map->size++;
			}
			// printf("inserted!\n" );
			pthread_rwlock_unlock(&map->lock) ;
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

			pthread_rwlock_rdlock(&map->lock);
			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("key found!\n" );
				pthread_rwlock_unlock(&map->lock) ;
				return temp->value;
			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("key found!\n" );
						pthread_rwlock_unlock(&map->lock) ;
						return temp->value;
					}
					temp = temp->next;
				}
			}
			// printf("key not found\n" );
			pthread_rwlock_unlock(&map->lock) ;
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

			pthread_rwlock_rdlock(&map->lock);

			// if first one is matched
			if(map->cmp(temp->key,key)==0){
				// printf("entry found!\n" );
				pair* p = mem_alloc(sizeof(pair));
				p->key = temp->key;
				p->value  =temp->value;
				pthread_rwlock_unlock(&map->lock) ;
				return p;


			}else{
				temp = temp-> next;
				while(temp != NULL){
					if(map->cmp(temp->key,key)==0){
						// printf("entry found!\n" );
						pair* p = mem_alloc(sizeof(pair));
						p->key = temp->key;
						p->value  =temp->value;
						pthread_rwlock_unlock(&map->lock) ;
						return p;

					}
					temp = temp->next;
				}
			}
			// printf("entry not found\n" );
			pthread_rwlock_unlock(&map->lock) ;
			return NULL;

}

void* hashmap_remove_value(struct hashmap* map,
		void* key) {

			pthread_rwlock_wrlock(&map->lock);
			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_value\n" );
				pthread_rwlock_unlock(&map->lock) ;
				return NULL;
			}

			register node* prev = NULL;


			// if first index matched
			if(map->cmp(temp->key,key)==0){
				void * value = temp->value;
				map->array[index]  =temp->next;
				map->key_del(temp->key);
				// mem_free(temp);
				map->size--;
				pthread_rwlock_unlock(&map->lock) ;
				return value;
			}else{
				prev = temp;
				temp = temp->next;
				while(temp!=NULL){

					if(map->cmp(temp->key,key)==0){

						void * value = temp->value;

						prev->next = temp->next;

						map->key_del(temp->key);
						// mem_free(temp);
						// printf("value returned, key freed\n" );
						map->size--;
						pthread_rwlock_unlock(&map->lock) ;
						return value;
					}
					prev = temp;
					temp = temp->next;
				}
			}

			// printf("not found for remove_value\n" );
			pthread_rwlock_unlock(&map->lock) ;
			return NULL;
}

void* hashmap_remove_entry(struct hashmap* map,
		void* key) {

			pthread_rwlock_wrlock(&map->lock);

			int index = map->hash(map,key)% map->arraysize;
			register node* temp = map->array[index];
			if(temp == NULL){
				// printf("not found for remove_entry\n" );
				pthread_rwlock_unlock(&map->lock) ;
				return NULL;
			}

			register node* prev = NULL;
			// if first index matched
			if(map->cmp(temp->key,key)==0){

				pair* p = mem_alloc(sizeof(pair));
				p->key = temp->key;
				p->value  =temp->value;

				map->array[index]=temp->next;

				map->key_del(temp->key);
				map->val_del(temp->value);
				mem_free(temp);
				// printf("removed node, key + value returned\n" );
				map->size--;
				pthread_rwlock_unlock(&map->lock) ;
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
						map->key_del(temp->key);
						map->val_del(temp->value);
						mem_free(temp);
						// printf("removed node, key + value returned\n" );
						map->size--;
						pthread_rwlock_unlock(&map->lock) ;
						return p;
					}
					prev = temp;
					temp = temp->next;
				}
			}
			// printf("not found for remove_entry\n" );
			pthread_rwlock_unlock(&map->lock) ;
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
			if(prev->key != NULL){
				map->key_del(prev->key);
			}
			if(prev->value != NULL){
				map->val_del(prev->value);
			}
			// map->key_del(prev->key);
			// map->val_del(prev->value);
			mem_free(prev);
		}
	}
	mem_free(map->array);
	// printf("freed!\n" );
	pthread_rwlock_destroy(&map->lock);
	return;
}

// for thread and testing =======================================================
// ==============================================================================
size_t djb2_hash(struct hashmap* map, void* k) {
	size_t sz =map->objsize ; //replace this with the key size
	char* p = k;
	size_t hash = 5381;

	for(int i = 0; i < sz; i++) {
		hash = ((hash << 5) + hash) + p[i];
	}
	return hash;
}

void keyDel(void* a){
  // mem_free(a);
	a = NULL;
	return;
}
void valDel(void* a){
    // mem_free(a);
		a = NULL;
		return;
}
int compare(void* lhs, void* rhs){


		return strcmp(lhs,rhs);
}
void* write_insert(void* arg){
	thread_data* data = arg;

	for(int i=0; i<data->total_thread; i++){

		if(i != 0 && data->index % i ==0){
			hashmap_insert(data->map,"aa","aaa");
		}
		hashmap_insert(data->map,"bb","bbb");
	}


	return NULL;

}
void* write_remove(void* arg){
	thread_data* data = arg;

	for(int i=0; i<data->total_thread; i++){
		if(i != 0 &&  data->index % i ==0){
			hashmap_remove_entry(data->map, "aa");
		}
		hashmap_remove_value(data->map, "bb");
	}

	return NULL;

}
void* read_get(void* arg){
	thread_data* data = arg;

	for(int i=0; i<data->total_thread; i++){
		if(i != 0 &&  data->index % i ==0){
			hashmap_get_entry(data->map, "aa");
		}
		hashmap_get(data->map, "bb");
	}

	return NULL;
}

// int main(){
// 	hashmap* map = mem_alloc(sizeof(hashmap));
//
//   int thread_no = 2;
//   size_t totalsize = 100;
// 	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);
//   pthread_rwlock_init(&map->lock,NULL);
//
//   pthread_t threads[thread_no];
//   thread_data *data = mem_alloc(sizeof(thread_data)*thread_no);
//   for(int i=0; i<thread_no; i++){
//     data[i].map = map;
//     data[i].index = random()%totalsize;
//     data[i].total_thread = totalsize;
//   }
//   pthread_create(&threads[0],NULL,write_insert,&data[0]);
//   pthread_create(&threads[1],NULL,read_get,&data[1]);
//
//   for(int j=0 ; j<thread_no ; j++){
//     pthread_join(threads[j],NULL);
//   }
//
//   printf("%ld\n",map->size );
//
//   hashmap_destroy(map);
//   mem_free(map);
//   mem_free(data);
// 	return 0;
// }
