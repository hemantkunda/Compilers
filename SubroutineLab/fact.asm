# Hemant Kunda
# Uses a subroutine to calculate the factorial of a number.
# Version #1
.text 0x00400000
.globl main
main:
li $v0, 4
la $a0, prompt
syscall # print the prompt to the screen
li $v0, 5
syscall # input a number
move $a0, $v0
subu $sp, $sp, 4
sw $ra, ($sp) # push
jal fact
lw $ra, ($sp)
addu $sp, $sp, 4 # pop
move $a1, $v0
li $v0, 4
la $a0, desc
syscall # print the description of what's happening
li $v0, 1
move $a0, $a1
syscall
li $v0, 10
syscall # return

fact: 
bne $a0, 0, continue
li $v0, 1
j return
continue:
subu $sp, $sp, 4
sw $a0, ($sp) # push the argument onto the stack
subu $a0, $a0, 1
subu $sp, $sp, 4
sw $ra, ($sp) # push
jal fact
lw $ra, ($sp)
addu $sp, $sp, 4 # pop
lw $a0, ($sp)
addu $sp, $sp, 4 # pop the argument off the stack
mult $v0, $a0
mflo $v0
return: 
jr $ra


.data
prompt: 
.asciiz "Enter a number for its factorial to be computed.\n"
desc:
.asciiz "Printing the factorial: "