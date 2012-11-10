//Arup Guha
//7/5/04 for 2004 BHCSI
//BigInt Example using an Array: This class allows the user to create 
//unsigned Big Integers, and add and subtract them.
package com.badlogic.androidgames.framework.math;

import java.io.*;

public class BigInt {

public int[] digits = new int[10]; // Stores the digits
public int size; // Stores the actual length of the BigInt in digits.
public int intValue;

public BigInt(int n) {

 intValue = n;
 
 // Special case of n=0.
 if (n == 0) {
   size = 1;
   digits[0] = 0;
 }

 else {

   int numdigits = 0;
   int saven = n;

   // Count the number of digits.
   while (n > 0) {
     numdigits++;
     n /= 10;
   }
   size = numdigits; // Set size.
   digits = new int[size]; // Allocate space.

   // Peel off the digits starting with the least significant one, one
   // by one. The least significant digit is stored in index 0.
   for (int i=0; i<size; i++) {
     digits[i] = saven % 10;
     saven /= 10;
   }
 }
      
}

// Builds a BigInt object from a string n that can NOT have a leading 0,
// unless the value represented is 0.
public BigInt(String n) {

 size = n.length();
 digits = new int[size];
 for (int i=0; i<size; i++)
   digits[i] = (int)(n.charAt(size-1-i) - '0');
}

// Creates a BigInt object from an array of digits, where the least
// significant digit is stored in array index 0.
public BigInt(int[] dig) {

 // If the entire array is full of zeroes, create 0.
 if (zero(dig)) {
   size = 1;
   digits[0] = 0;
 }

 else {

   // Strip off leading zeroes, automatically adjusting size.
   size = dig.length;
   while (dig[size-1] == 0)
     size--;

   // Copy the rest of the digits appropriately.
   digits = new int[size];
   for (int i=0; i<size; i++)
     digits[i] = dig[i];

 }
}

// Returns true iff all the digits stored in numbers are 0.
public static boolean zero(int[] numbers) {
 for (int i=0; i<numbers.length; i++)
   if (numbers[i] != 0)
     return false;
 return true;
}

// Adds other to the current object and returns the answer.
public BigInt add(BigInt other) {

 // Create an array that is large enough to store the answer.
 int newsize = max(size, other.size) + 1;
 int [] tempdigits = new int[newsize];

 // Initialize result and carry.
 int result=0, carry=0;

 // Loop through all the digits of both numbers, one place at a time.
 for (int i=0; i<newsize-1; i++) {

   int temp = 0; // Set the temp sum.

   // If necessary add in the contribution of the current object.
   if (i < size)
     temp += digits[i];

   // If necessary add in the contribution of other.
   if (i < other.size)
     temp += other.digits[i];

   temp += carry; // Add in the carry from the last iteration.

   // Record the result and the carry.
   tempdigits[i] = temp % 10;
   carry = temp/10;
 }
 
 // Set the last digit.
 if (carry == 1)
   tempdigits[newsize-1] = 1;
 else
   tempdigits[newsize-1] = 0;

 // Return the answer.
 return new BigInt(tempdigits);
}

// Subtracts other from the current object and returns the answer. Since
// this is only an unsigned implementation, other MUST BE smaller than
// the current object.
public BigInt sub(BigInt other) {

 // Allocate enough space to store the solution.
 int [] tempdigits = new int[size];

 // Initialize result and carry.
 int result=0, carry=0;

 // Loop through all the digits, one by one.
 for (int i=0; i<size; i++) {
   
   // Initialize the answer to the iteration to the digit of current.
   // It's guaranteed to exist since we know the current object stores
   // a larger number.
   int temp = digits[i];

   // Subtract the appropriate digit if necessary/
   if (i < other.size)
     temp -= other.digits[i];

   // Subtract the carry from the last iteration.
   temp -= carry;
   
   carry = 0; // Reset for future calculations.

   // If temp is too small, you need to borrow.
   if (temp < 0) {
     temp = temp + 10;
     carry = 1;
   }
   
   // Set the current digit - carry has already been set.
   tempdigits[i] = temp;
 }

 // return the answer.
 return new BigInt(tempdigits);
}

// Returns -1 if the current object is less than other, 0 if they are 
// equal or 1 if the current object is greater than other.
public int compare(BigInt other) {

 // Easy cases where the two numbers have a different # of digits.
 if (size < other.size)
   return -1;
 else if (size > other.size)
   return 1;
 
 // Compare from the most significant digit down, returning as soon as
 // a discrepancy is found.
 for (int i=size-1; i>=0; i--) {
   if (digits[i] < other.digits[i])
     return -1;
   else if (digits[i] > other.digits[i])
     return 1;
 }

 // All digits must have matched, the two values are equal.
 return 0;

}

// Returns the greater of a and b.
public static int max(int a, int b) {
 if (a > b)
   return a;
 else
   return b;
}

// Returns a string with the digits in the normal order.
public String toString() {
 String ans = new String();
 for (int i=0; i<size; i++)
   ans = charToString((char)(digits[i]+'0')) + ans;
 return ans;
}

// Returns the equivalent String object to c.
public static String charToString(char c) {
 char[] temp = new char[1];
 temp[0] = c;
 return new String(temp);
}

// Runs a very simple test.
public static void main(String[] args) throws IOException {

 BigInt one = new BigInt("912021312093213892");
 BigInt two =           new BigInt(987654321);
 BigInt three = new BigInt("912021312093213891");

 BigInt four = one.add(two);
 BigInt five = four.sub(three);
 int cmp = one.compare(three);

 System.out.println("One is "+one);
 System.out.println("Two is "+two);
 System.out.println("Three is "+three);
 System.out.println("Four is "+four);
 System.out.println("Five is "+five);
 System.out.println("Cmp is "+cmp);

}
}
