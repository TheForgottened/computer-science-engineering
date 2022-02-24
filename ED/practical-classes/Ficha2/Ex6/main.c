#include <stdio.h>
#include <stdlib.h>

int binaryIterative(size_t size, int array[size], int key);
int binaryRecursive(size_t size, int array[size], int key);

int numbersInRangeIterative(size_t size, int array[size], int min, int max);
int numbersInRangeRecursive(size_t size, int array[size], int min, int max);

int main() {
    setbuf(stdout, NULL);

    int ar[] = {1, 4, 5, 7, 8, 11, 19};
    size_t arLength = sizeof(ar) / sizeof(ar[0]);

    int low = -100;
    int high = 6;

    int ansIterative = numbersInRangeIterative(arLength, ar, low, high);
    printf("Interative answer: %i\n", ansIterative);

    int ansRecursive = numbersInRangeRecursive(arLength, ar, low, high);
    printf("Recursive answer: %i\n", ansRecursive);

    return 0;
}

int numbersInRangeIterative(size_t size, int array[size], int min, int max) {
    return abs(binaryIterative(size, array, max) + 1) - abs(binaryIterative(size, array, min) + 1);
}

int numbersInRangeRecursive(size_t size, int array[size], int min, int max) {
    return abs(binaryRecursive(size, array, max) + 1) - abs(binaryRecursive(size, array, min) + 1);
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
