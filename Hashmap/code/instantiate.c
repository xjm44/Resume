#include <stdlib.h>
#include "hashmap.h"
#include "instantiate.h"

struct hashmap* hashmap_construct() {
	return malloc(sizeof(struct hashmap));
}
