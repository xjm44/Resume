The codes I have finished are basically in the hashmap.c and hashmap.h, which are required.

These are the .c files I have created, for testing and comparing purposes: (not really relevant for marking because the codes that need to be marked are in the hashmap.c and hashmap.h already)
hashmap_atomic.c
hashmap_barrier.c
hashmap_fg.c (fg means fine-grained)
hashmap_mutex.c
hashmap_sem.c
hashmap_spinlock.c
hashmap_test.c


As for the Makefile, the command "make test_from_teacher" will generate "test_from_teacher". This is to test my codes against the provided benchmark (maptest.o).

The command "make test_from_own" will generate "test_from_own". This is the test-cases I have created.

As for the rest in the Makefile, they are used for optimistic locking purposes.

When I implementing the (core) functions, I created 3 structs beforehand.
The 3 structs are about the hashmap, node (which is inside the map), and pair (to store the key + value when *_entry is performed).

There are many types of locks in the hashmap struct. However, pthread_rwlock_t lock is the one used in hashmap.c. The rest are just for comparing purposes.

For the init, I set the max-number of the nodes, which can be stored in the hashmap, as the "size" in the parameter.

As for the functions, which are compulsory to implement, I have made them thread-safe, by using the locks adequately.

data.txt is the evidence of comparing the given test cases and the one I generated (between the given test and my own which generated from my hashmap.c). This is used to generate out.png.

data1.txt is the evidence of comparing different types of locks, for optimistic locking purposes. This is used to generate out1.png.

When I find the timings, I can't use "CMOCKA_MESSAGE_OUTPUT=xml" in my virtual machine, even after I used commands like "sudo apt-get install libcmocka-dev". Hence, I used "time ./test" to find the timings. 
