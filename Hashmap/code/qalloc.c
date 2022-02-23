#include "qalloc.h"


void* mem_alloc(size_t size) {
    return malloc(size);
}

void mem_free(void* ptr) {
    return free(ptr);
}

void* mem_calloc(size_t nmemb, size_t size) {
    return calloc(nmemb, size);
}

void* mem_realloc(void* ptr, size_t size) {
    return realloc(ptr, size);
}

void* mem_reallocarray(void* ptr, size_t nmemb, size_t size) {
    return reallocarray(ptr, nmemb, size);
}
