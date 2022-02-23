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

#include <setjmp.h>
#include <assert.h>
#include<stdarg.h>
#include <cmocka.h>

// size_t djb2_hash(struct hashmap* map, void* k) {
// 	size_t sz =map->objsize ; //replace this with the key size
// 	char* p = k;
// 	size_t hash = 5381;
//
// 	for(int i = 0; i < sz; i++) {
// 		hash = ((hash << 5) + hash) + p[i];
// 	}
// 	return hash;
// }
//
// void keyDel(void* a){
//   // mem_free(a);
// 	a = NULL;
// 	return;
// }
// void valDel(void* a){
//     // mem_free(a);
// 		a = NULL;
// 		return;
// }
// int compare(void* lhs, void* rhs){
//
//
// 		return strcmp(lhs,rhs);
// }

// // for checking the map
// void printAll(hashmap* map){
// 	node* temp =NULL;
// 	printf("==============================\n" );
// 	node* node= NULL;
//
// 	for(int i=0; i<map->arraysize; i++){
// 		node = map->array[i];
// 		if(node!=NULL){
// 			temp = node;
// 			while(temp!=NULL){
// 				printf("[%d] key: %s __ value: %s\n",i, (char*)temp->key, (char*)temp->value );
// 				temp = temp->next;
// 			}
// 			printf("\n");
// 		}
// 	}
// }
static void simple_test_insert(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));
  assert_non_null(map);
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,3);

  // srand(time(NULL));
  for(int i=0; i<totalsize; i++){
    // generate different keys.
    char* key = malloc(sizeof(char*));
    char ranchar1 = 'a' +(random()%26);
    char ranchar2 = 'a' +(random()%26);
    char ranchar3 = 'a' +(random()%26);
    key[0]=ranchar1;
    key[1]=ranchar2;
    key[2]=ranchar3;

    // generate different values.
    char* val = malloc(sizeof(char*));
    char ranchar11 = 'a' +(random()%26);
    char ranchar21 = 'a' +(random()%26);
    char ranchar31 = 'a' +(random()%26);
    char ranchar41 = 'a' +(random()%26);
    char ranchar51 = 'a' +(random()%26);
    val[0]=ranchar11;
    val[1]=ranchar21;
    val[2]=ranchar31;
    val[3]=ranchar41;
    val[4]=ranchar51;

    hashmap_insert(map,key,val);

  }

  // printf("%d\n",map->size );
  hashmap_insert(map,"aa","helloa");
  assert_ptr_equal(hashmap_get(map,"aa"), "helloa");
  assert_int_not_equal(map->size, totalsize);

  hashmap_insert(map,"aa","zaza");
  assert_ptr_equal(hashmap_get(map,"aa"), "zaza");

  hashmap_destroy(map);
  mem_free(map);

}

static void simple_test_get(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));
  assert_non_null(map);
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);

  // srand(time(NULL));
  for(int i=0; i<totalsize; i++){
    if(i%7 == 0){
      hashmap_insert(map,"aa","helloa");
    }
    if(i%4 ==0 && i != 0){
      hashmap_insert(map,"bb","hellob");
    }
    if(i%3 ==0 && i != 0){
      hashmap_insert(map,"cc","helloc");
    }
    if(i%8 ==0 && i != 0){
      hashmap_insert(map,"dd","hellod");
    }
    if(i%5 ==0 && i != 0){
      hashmap_insert(map,"ee","helloe");
    }
    hashmap_insert(map,"ff","hellof");
  }
  assert_ptr_equal(hashmap_get(map,"aa"), "helloa");
  assert_int_equal(map->arraysize, 100);
  assert_ptr_equal(hashmap_get(map,"ee"), "helloe");
  assert_null(hashmap_get(map,"23"));
  assert_non_null(hashmap_get(map,"bb"));

  assert_null(hashmap_get_entry(map,"23"));
  assert_non_null(hashmap_get_entry(map,"cc"));

  pair* p = mem_alloc(sizeof(pair));
  p = hashmap_get_entry(map,"dd");
  assert_ptr_equal(p->key, "dd");
  assert_ptr_equal(p->value, "hellod");

  mem_free(p);
  hashmap_destroy(map);
  mem_free(map);

}
static void simple_test_remove(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));
  assert_non_null(map);
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);


  hashmap_insert(map,"ff","hellof");
  hashmap_insert(map,"aa","helloa");
  hashmap_insert(map,"bb","hellob");
  hashmap_insert(map,"cc","helloc");
  hashmap_insert(map,"dd","hellod");
  hashmap_insert(map,"ee","helloe");


  assert_int_equal(map->size, 106);
  hashmap_remove_entry(map,"aa");
  assert_int_equal(map->size, 105);
  hashmap_remove_entry(map,"bb");
  assert_int_equal(map->size, 104);


  pair* p = mem_alloc(sizeof(pair));
  p = hashmap_remove_entry(map,"cc");
  assert_ptr_equal(p->key, "cc");
  assert_ptr_equal(p->value, "helloc");
  assert_int_equal(map->size, 103);

  void* value = NULL;
  value = hashmap_remove_value(map,"dd");
  assert_ptr_equal(value, "hellod");

  assert_int_equal(map->size, 102);

  void* value1 = NULL;
  value1 = hashmap_remove_value(map,"zz");
  assert_null(value1);
  assert_int_equal(map->size, 102);

  assert_null(hashmap_remove_entry(map,"z2"));


  mem_free(p);
  hashmap_destroy(map);
  mem_free(map);

}
static void simple_test_size(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));
  assert_non_null(map);
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);


  hashmap_insert(map,"ff","hellof");
  hashmap_insert(map,"aa","helloa");
  hashmap_insert(map,"bb","hellob");
  hashmap_insert(map,"cc","helloc");
  hashmap_insert(map,"dd","hellod");
  hashmap_insert(map,"ee","helloe");


  assert_int_equal(map->size, 106);
  hashmap_remove_entry(map,"aa");
  assert_int_equal(map->size, 105);

  void* value = NULL;
  value = hashmap_remove_value(map,"dd");
  assert_ptr_equal(value, "hellod");
  assert_int_equal(map->size, 104);

  assert_int_equal(map->arraysize, 100);

  hashmap_insert(map,"zz","helloz");
  assert_int_equal(map->size, 105);

  hashmap_insert(map,"zz","zzz");
  assert_int_equal(map->size, 105);

  hashmap_remove_entry(map,"62");
  assert_int_equal(map->size, 105);

  hashmap_remove_value(map,"62");
  assert_int_equal(map->size, 105);

  hashmap_destroy(map);
  mem_free(map);

}

static void simple_test_update(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));
  assert_non_null(map);
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);


  hashmap_insert(map,"ff","hellof");
  hashmap_insert(map,"aa","helloa");
  hashmap_insert(map,"bb","hellob");
  hashmap_insert(map,"cc","helloc");
  hashmap_insert(map,"dd","hellod");
  hashmap_insert(map,"ee","helloe");


  pair* p = mem_alloc(sizeof(pair));
  p = hashmap_get_entry(map,"cc");
  assert_ptr_equal(p->key, "cc");
  assert_ptr_equal(p->value, "helloc");

  hashmap_insert(map,"cc","ccc");

  pair* p1 = mem_alloc(sizeof(pair));
  p1 = hashmap_get_entry(map,"cc");
  assert_ptr_equal(p1->key, "cc");
  assert_ptr_equal(p1->value, "ccc");


  assert_ptr_equal(hashmap_get(map,"dd"), "hellod");

  hashmap_insert(map,"dd","dddd");

  assert_ptr_equal(hashmap_get(map,"dd"), "dddd");

  assert_null(hashmap_get_entry(map,"42"));
  assert_null(hashmap_get(map,"42"));;


  mem_free(p);
  mem_free(p1);
  hashmap_destroy(map);
  mem_free(map);

}

static void concurrent_test_insert(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));

  int thread_no = 2;
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);
  pthread_rwlock_init(&map->lock,NULL);

  pthread_t threads[thread_no];
  thread_data *data = mem_alloc(sizeof(thread_data)*thread_no);
  for(int i=0; i<thread_no; i++){
    data[i].map = map;
    data[i].index = random()%totalsize;
    data[i].total_thread = totalsize;
  }
  pthread_create(&threads[0],NULL,write_insert,&data[0]);
  pthread_create(&threads[1],NULL,read_get,&data[1]);

  for(int j=0 ; j<thread_no ; j++){
    pthread_join(threads[j],NULL);
  }

  assert_string_equal(hashmap_get(map,"aa"), "aaa");
  assert_string_equal(hashmap_get(map,"bb"), "bbb");

  hashmap_destroy(map);
  mem_free(map);
  mem_free(data);

}

static void concurrent_test_remove(void** state){
  hashmap* map = mem_alloc(sizeof(hashmap));
  assert_non_null(map);


  int thread_no = 2;
  size_t totalsize = 100;
	hashmap_init(map,djb2_hash,keyDel,valDel,compare,totalsize,2);
  pthread_rwlock_init(&map->lock,NULL);

  pthread_t threads[thread_no];
  thread_data *data = mem_alloc(sizeof(thread_data)*thread_no);
  for(int i=0; i<thread_no; i++){
    data[i].map = map;
    data[i].index = random()%totalsize;
    data[i].total_thread = totalsize;
  }
  pthread_create(&threads[0],NULL,write_insert,&data[0]);
  pthread_create(&threads[1],NULL,write_remove,&data[1]);

  for(int j=0 ; j<thread_no ; j++){
    pthread_join(threads[j],NULL);
  }

  assert_in_range(map->size,100,200);
  assert_null(hashmap_get(map,"cc"));

  hashmap_destroy(map);
  mem_free(map);
  mem_free(data);

}

static void simple_test(void** state){
    hashmap* map = mem_alloc(sizeof(hashmap));
    assert_non_null(map);
    hashmap_init(map, djb2_hash,keyDel,valDel,compare, 100, sizeof(ssize_t));

    for(ssize_t i = 0; i < 15; i++) {
        ssize_t* key = malloc(sizeof(ssize_t));
        ssize_t* val = malloc(sizeof(ssize_t));
        *key = i;
        *val = i;

        hashmap_insert(map, key, val);
    }

    for(ssize_t i = 0; i < 15; i++) {
        ssize_t* val  = hashmap_get(map, &i);
        assert_int_equal(i, *val);
    }

    hashmap_destroy(map);
    free(map);
}

int main(){




  const struct CMUnitTest tests[] = {

    cmocka_unit_test(simple_test_insert),
    cmocka_unit_test(simple_test_get),
    cmocka_unit_test(simple_test_remove),
    cmocka_unit_test(simple_test_size),
    cmocka_unit_test(simple_test_update),
    cmocka_unit_test(concurrent_test_insert),
    cmocka_unit_test(concurrent_test_remove),
    cmocka_unit_test(simple_test),
  };

  return cmocka_run_group_tests(tests,NULL,NULL);


}
