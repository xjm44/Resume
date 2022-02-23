
//djb2, simple
static size_t djb2_hash(struct hashmap* map, void* k) {
	size_t sz = 0; //replace this with the key size
	char* p = k;
	size_t hash = 5381;
	for(int i = 0; i < sz; i++) {
		hash = ((hash << 5) + hash) + p[i];
	}
	return hash;
}


