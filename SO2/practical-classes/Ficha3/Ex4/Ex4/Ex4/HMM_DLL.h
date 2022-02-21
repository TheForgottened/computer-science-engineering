// SO2_F3_MyDynamic_DLL.h - Contains declarations of the SO2_F3_MyDynamic_DLL functions

/*
Use __cplusplus preprocessor macro to determine
which language is being compiled.

If we use this technique and provide header files
for the DLL, these functions can be used by C and C++
users with no change

#ifdef __cplusplus
extern "C" {
#endif

// Your functions that can be accessible from
// C or C++ projects

#ifdef __cplusplus
}
#endif
*/

#pragma once

#ifdef __cplusplus  
extern "C" {
#endif  

#ifdef EX4_EXPORTS
#define HMM_DLL_API __declspec(dllexport)
#else
#define HMM_DLL_API __declspec(dllimport)
#endif

	// Set the factor variable
	HMM_DLL_API double factor;

	// Function to applyFactor
	HMM_DLL_API double applyFactor(double v);

#ifdef __cplusplus  
}
#endif
