# Hemant Kunda
# Uses two subroutines to determine the biggest of three numbers.
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
li $v0, 5
syscall # input a number
move $a3, $v0
subu $sp, $sp, 4
sw $ra, ($sp) # push
jal max3 # call the max3 function
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

max3:
subu $sp, $sp, 4
sw $ra, ($sp) # push
jal max2
lw $ra, ($sp)
addu $sp, $sp, 4 # pop
move $a1, $v0
move $a2, $a3
subu $sp, $sp, 4
sw $ra, ($sp) # push
jal max2
lw $ra, ($sp)
addu $sp, $sp, 4 # pop
jr $ra

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
.asciiz "Enter three numbers to be compared.\n"
desc:
.asciiz "Printing the largest number: "