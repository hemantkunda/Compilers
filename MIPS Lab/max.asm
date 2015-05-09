# Hemant Kunda
# Takes in two numbers from the user and prints the larger one to the console.
.text 0x00400000
.globl main
main:

li $v0, 4 
la $a0, prompt
syscall # print the prompt to the screen
li $v0, 5
syscall # input a number
move $t0, $v0 # move the inputted value to $t0
li $v0, 5
syscall # input a number
move $t1, $v0 # move the inputted value to $t1
bge $t0, $t1, first
move $t2, $t1
j print
first: 
move $t2, $t0
print:
li $v0, 4
la $a0, desc
syscall # print the description of what's happening

li $v0, 1
move $a0, $t2
syscall # print the larger number
li $v0, 10
syscall # exit

.data
prompt: 
.asciiz "Enter two numbers to be compared.\n"
desc:
.asciiz "Printing the larger number: "