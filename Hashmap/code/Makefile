CC=gcc
CFLAGS=-g -std=gnu11 -Wall -Werror -O0
TARGET=hashmap
OBJS=hashmap.c
.PHONY: clean
all: $(TARGET)

hashmap: hashmap.c hashmap.h
	$(CC) $(CFLAGS) $(OBJS) qalloc.c -o $@ -lpthread

test_from_teacher:
	gcc -O0 -g maptest.o hashmap.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka

test_from_teacher_fine_g:
	gcc -O0 -g maptest.o hashmap_fg.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka

test_from_teacher_mutex:
	gcc -O0 -g maptest.o hashmap_mutex.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka

test_from_teacher_sem:
	gcc -O0 -g maptest.o hashmap_sem.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka

test_from_teacher_barrier:
	gcc -O0 -g maptest.o hashmap_barrier.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka

test_from_teacher_atomic:
	gcc -O0 -g maptest.o hashmap_atomic.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka 

test_from_teacher_spinlock:
	gcc -O0 -g maptest.o hashmap_spinlock.c instantiate.c qalloc.c -o $@ -lpthread -lcmocka

test_from_own:
	gcc -O0 -g -Wall hashmap_test.c qalloc.c hashmap.c -lpthread -lcmocka -o $@

clean:

	rm -f test_from_teacher
	rm -f test_from_teacher_fine_g
	rm -f test_from_teacher_mutex
	rm -f test_from_teacher_sem
	rm -f test_from_teacher_barrier
	rm -f test_from_teacher_atomic
	rm -f test_from_teacher_spinlock
	rm -f hashmap
	rm -f test_from_own
	rm -f a.out
