/*
 * Type symbol for the hashmap, implementation
 * is defined by the student but must represent a hashmap
 */

#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>
/* forward type definitions */
typedef struct hashmap hashmap;
typedef struct node node;
typedef struct pair pair;
typedef struct thread_data thread_data;
typedef struct lockk lockk;
typedef void(*key_del)(void*);
typedef void(*val_del)(void*);
typedef int(*cmp)(void*, void*);
typedef size_t(*hash)(struct hashmap*,void*);
// =============================



struct hashmap{
	size_t size;
	node** array;

	void(*key_del)(void*);
  void(*val_del)(void*);
  int(*cmp)(void*, void*);
	size_t(*hash)(struct hashmap*,void*);

	size_t objsize;

	// ============= For different threads ====================
	pthread_rwlock_t lock;
	// pthread_mutex_t lock;
	// pthread_barrier_t lock;
	// sem_t lock;
	// struct lockk* lock;
	// pthread_spinlock_t lock;
	// ========================================================

	size_t arraysize;
};

struct node{
	void* key;
	void* value;
	node* next;
	pair* pair;
};

struct pair{
	void* key;
	void* value;
};
//=============== For atomic operation
#define LOCKED 1
#define UNLOCKED 0
struct lockk{
  int lock;
  int count;
};
/**
 * Initialises your data structure with the following
 * arguments.
 */
void hashmap_init(struct hashmap* map,
		size_t(*hash)(struct hashmap*,void*),
		void(*key_del)(void*),
		void(*val_del)(void*),
		int(*cmp)(void*, void*),
		size_t size, size_t keysize);

/**
 * Inserts a key and value
 */
void hashmap_insert(struct hashmap*,
		void* key,
		void* value);

void* hashmap_get(struct hashmap* map,
		void* key);

void* hashmap_get_entry(struct hashmap* map,
		void* key);

void* hashmap_remove_value(struct hashmap* map,
		void* key);

void* hashmap_remove_entry(struct hashmap* map,
		void* key);

size_t hashmap_size(struct hashmap* map);

void hashmap_destroy(struct hashmap* map);

// for testing ====================================
void printAll(hashmap* map);
size_t djb2_hash(struct hashmap* map, void* k);
void keyDel(void* a);
void valDel(void* a);
int compare(void* lhs, void* rhs);
struct thread_data{
	hashmap* map;
	int index;
	int total_thread;
};
void* write_insert(void* arg);
void* write_remove(void* arg);
void* read_get(void* arg);
// ================================================
