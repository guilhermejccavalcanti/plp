-- testando operadores booleanos
print(10 or 20)   --> 10
print(nil or "a") --> "a"
print(nil and 10) --> nil
print(10 and 20)  --> 20

-- testando escopos e overloading de funções
function t()
 i = 10
 local i = 5
 i=i+1
 print(i)    	--> 6
end

t()
print(i)  		--> 10

function t()
  	print(1) 	--> 1
	print(2)    --> 2	
end

t()

-- testando múltiplos retornos
function t2()
 return "asd",23
end

a,b = t2()

print(a)  --> asd
print(b)  --> 23
