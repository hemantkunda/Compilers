	# Hemant Kunda
	# Generated via Pascal to MIPS compiler
	# Compilers 2014-2015 S2
	.text
	.globl main
	main: 
	la $t0, varcount
	subu $sp, $sp, 4
	sw $t0, ($sp) # pushing $t0 to stack
	li $v0, 1
	lw $t0, ($sp)
	addu $sp, $sp, 4 # popping to $t0 from stack
	sw $v0, ($t0)
WhileStatement1:
	la $t0, varcount
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp) # pushing $v0 to stack
	li $v0, 15
	lw $t0, ($sp)
	addu $sp, $sp, 4 # popping to $t0 from stack
	# left in $t0, right in $v0
	bgt $t0, $v0, endWhile1
	la $t0, varcount
	lw $v0, ($t0)
	move $a0, $v0
	li $v0, 1
	syscall
	li $v0, 4
	la $a0, nl
	syscall
	la $t0, varcount
	subu $sp, $sp, 4
	sw $t0, ($sp) # pushing $t0 to stack
	la $t0, varcount
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp) # pushing $v0 to stack
	li $v0, 1
	lw $t0, ($sp)
	addu $sp, $sp, 4 # popping to $t0 from stack
	# left in $t0 and right in $v0
	addu $v0, $t0, $v0 # adds the values and stores in $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4 # popping to $t0 from stack
	sw $v0, ($t0)
	j WhileStatement1
endWhile1:
	li $v0, 10
	syscall
	.data
nl:
	.asciiz "\n"
varcount:
	.word 0
