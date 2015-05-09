# Hemant Kunda
# Uses a subroutine to determine the bigger of two numbers.
# Version #1
.text 0x00400000
.globl main
main:

li $v0, 4
la $a0, prompt
syscall # print the prompt to the screen
li $v0, 5
syscall # input a number
move $a1, $v0
li $v0, 5
syscall # input a number
move $a2, $v0
subu $sp, $sp, 4
sw $ra, ($sp) # push
jal max2 # call the max2 function
move $a1, $v0
lw $ra, ($sp)
addu $sp, $sp, 4 # pop
li $v0, 4
la $a0, desc
syscall # print the description of what's happening
li $v0, 1
move $a0, $a1
syscall
li $v0, 10
syscall # return

max2: 
bge $a1, $a2, first
move $v0, $a2
j return
first: 
move $v0, $a1
return: 
jr $ra

.data
prompt: 
.asciiz "Enter two numbers to be compared.\n"
desc:
.asciiz "Printing the larger number: "