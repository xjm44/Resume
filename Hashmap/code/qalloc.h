#ifndef QALLOC_H
#define QALLOC_H

#include <stdlib.h>

void* mem_alloc(size_t size);

void mem_free(void* ptr);

void* mem_calloc(size_t nmemb, size_t size);

void* mem_realloc(void* ptr, size_t size);

void* mem_reallocarray(void* ptr, size_t nmemb, size_t size);

#endif
