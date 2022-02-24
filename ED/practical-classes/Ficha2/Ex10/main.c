#include <stdio.h>
#include <stdlib.h>

int binaryIterative(size_t size, int array[size]);
int binaryRecursive(size_t size, int array[size]);

int main() {
    setbuf(stdout, NULL);

    int ar[] = {0, 1, 4, 5, 7, 8, 11, 19, -10, -8, -7};
    size_t arLength = sizeof(ar) / sizeof(ar[0]);

    int ansIterative = binaryIterative(arLength, ar);
    printf("Interative answer: %i\n", ansIterative);

    int ansRecursive = binaryRecursive(arLength, ar);
    printf("Recursive answer: %i\n", ansRecursive);

    return 0;
}

int binaryIterative(size_t size, int array[size]) {
    int mid;
    int start = 0;
    int end = size - 1;

    while (start <= end) {
        mid = (start + end) / 2;

        if (mid < array[mid]) return mid;
        if (end - start == 1) break;

        if (key < array[mid]) {
            end = mid - 1;
        } else {
            start = mid + 1;
        }
    }

    return -1;
}

int binaryRecursive_Private(int* array, int start, int end) {
    if (start > end) return -1;

    int mid = start + ((end - start) / 2);

    if (array[mid] > mid) return mid;

    if (array[mid] > key) return binaryRecursive_Private(array, start, mid - 1);

    return binaryRecursive_Private(array, mid + 1, end);
}

int binaryRecursive(size_t size, int array[size]) {
    return binaryRecursive_Private(array, 0, size - 1);
}