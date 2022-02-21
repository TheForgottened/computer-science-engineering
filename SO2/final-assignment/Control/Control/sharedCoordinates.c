#include "sharedCoordinates.h"


BOOL initCoordinatesArray(pCoordinatesStruct coordinatesArray, unsigned int size) {
	for (unsigned int i = 0; i < size; i++){
		coordinatesArray[i].x = -1;
		coordinatesArray[i].y = -1;
	}
	
	return TRUE;
}
