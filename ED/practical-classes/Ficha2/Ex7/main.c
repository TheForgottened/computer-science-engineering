#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

bool binaryIterative(size_t size, int array[size], int key);
bool binaryRecursive(size_t size, int array[size], int key);

int main() {
    setbuf(stdout, NULL);

    int ar[] = {1, 4, 5, 7, 8, 11, 19};
    size_t arLength = sizeof(ar) / sizeof(ar[0]);

    int key = 7;

    bool ansIterative = binaryIterative(arLength, ar, key);
    printf("Interative answer: %s\n", ansIterative ? "true" : "false");

    bool ansRecursive = binaryRecursive(arLength, ar, key);
    printf("Recursive answer: %s\n", ansRecursive ? "true" : "false");

    return 0;
}

bool binaryIterative(size_t size, int array[size], int key) {
    int mid;
    int start = 0;
    int end = size - 1;

    while (start <= end) {
        mid = (start + end) / 2;

        if (key == array[mid]) {
            switch (mid)    {
                case 0:
                    return array[mid] == array[mid + 1];
                    break;

                case 1:
                    return array[mid] == array[mid - 1];
                    break;
                
                default:
                    return (array[mid] == array[mid - 1]) || (array[mid] == array[mid + 1]);
                    break;
            }
        } 

        if (end - start == 1) break;

        if (key < array[mid]) {
            end = mid - 1;
        } else {
            start = mid + 1;
        }
    }

    return false;
}

bool binaryRecursive_Private(int* array, int key, int start, int end) {
    if (start > end) return - (start + 1);

    int mid = start + ((end - start) / 2);

    if (array[mid] == key) {
        switch (mid)    {
            case 0:
                return array[mid] == array[mid + 1];
                break;

            case 1:
                return array[mid] == array[mid - 1];
                break;
            
            default:
                return (array[mid] == array[mid - 1]) || (array[mid] == array[mid + 1]);
                break;
        }
    } 

    if (array[mid] > key) return binaryRecursive_Private(array, key, start, mid - 1);

    return binaryRecursive_Private(array, key, mid + 1, end);
}

bool binaryRecursive(size_t size, int array[size], int key) {
    return binaryRecursive_Private(array, key, 0, size - 1);
}
