#include <stdio.h>
#include <stdlib.h>

int binaryIterative(size_t size, int array[size], int key);
int binaryRecursive(size_t size, int array[size], int key);

int biggestSmallerThanKeyIterative(size_t size, int array[size], int key);
int biggestSmallerThanKeyRecursive(size_t size, int array[size], int key);

int main() {
    setbuf(stdout, NULL);

    int ar[] = {0, 1, 4, 5, 7, 8, 11, 19};
    size_t arLength = sizeof(ar) / sizeof(ar[0]);

    int key = 1;

    int ansIterative = biggestSmallerThanKeyIterative(arLength, ar, key);
    printf("Interative answer: %i\n", ansIterative);

    int ansRecursive = biggestSmallerThanKeyRecursive(arLength, ar, key);
    printf("Recursive answer: %i\n", ansRecursive);

    return 0;
}

int biggestSmallerThanKeyIterative(size_t size, int array[size], int key) {
    int index = binaryIterative(size, array, key);

    if (index == 0) return key;
    if (index < 0) index = abs(index + 1);

    return array[index - 1];
}

int biggestSmallerThanKeyRecursive(size_t size, int array[size], int key) {
    int index = binaryRecursive(size, array, key);

    if (index == 0) return key;
    if (index < 0) index = abs(index + 1);

    return array[index - 1];
}

int binaryIterative(size_t size, int array[size], int key) {
    int mid;
    int start = 0;
    int end = size - 1;

    while (start <= end) {
        mid = (start + end) / 2;

        if (key == array[mid]) return mid;
        if (end - start == 1) break;

        if (key < array[mid]) {
            end = mid - 1;
        } else {
            start = mid + 1;
        }
    }

    return - (start + 1);
}

int binaryRecursive_Private(int* array, int key, int start, int end) {
    if (start > end) return - (start + 1);

    int mid = start + ((end - start) / 2);

    if (array[mid] == key) return mid;

    if (array[mid] > key) return binaryRecursive_Private(array, key, start, mid - 1);

    return binaryRecursive_Private(array, key, mid + 1, end);
}

int binaryRecursive(size_t size, int array[size], int key) {
    return binaryRecursive_Private(array, key, 0, size - 1);
}