# Hemant Kunda
# Prints the numbers 1-10 to the console in a loop fashion.
.text 0x00400000
.globl main
main:

li $v0, 4 
la $a0, prompt
syscall # print the prompt to the screen
li $t0, 1
li $v0, 1
loop: 
bgt $t0, 10, end
move $a0, $t0
syscall # print the number to the screen
addu $t0, $t0, 1
li $v0, 4
la $a0, nl
syscall # print a new line character
li $v0, 1
j loop
end:
li $v0, 10
syscall # exit

.data
prompt: 
.asciiz "Printing the numbers from 1 to 10:\n"
nl:
.asciiz "\n"