--testando escopos
a = 2
c = 1

function inc(a)
	a = a + 1
	print(c)
	return a
end

print(a)

b = inc(a)

print(a)
print(b)
print(c)

--testando comando de controle
function testIfElseIfElse(i)
	if i == 0 then
		print("entrou no if")
	elseif i == 1 then
		print("entrou no primeiro elseif")
	elseif i == 2 then
		print("entrou no segundo elseif")
	else
		print("entrou no else")
	end
end

testIfElseIfElse(0)	
testIfElseIfElse(1)	
testIfElseIfElse(2)	
testIfElseIfElse(3)	

