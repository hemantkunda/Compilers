# Hemant Kunda
# Takes in 2 numbers from the user and adds them together, printing the sum out.
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
addu $a0, $t1, $t0 # store the sum of $t0 and $t1 in $a0
li $v0, 1
syscall # print the sum
li $v0, 10
syscall # exit

.data
prompt: 
.asciiz "Enter two numbers to be added.\n"