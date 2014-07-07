--testando precedência de operadores
x = (2+2)*2
print(x)				--8

x = 2+2*2
print(x)				--6

--testando atribuição múltipla
a,b,c = 1,2
print(a,b,c) 			-- 1,2,nil

--testando escopos
 x = 10               	-- variável global
 do                    	-- novo bloco
   local x = x         	-- novo 'x', com valor 10
   print(x)            	-- 10
   x = x+1
   do                  	-- outro bloco
	 local x = x+1     		-- outro 'x'
	 print(x)          	-- 12
   end
   print(x)            	-- 11
 end
 print(x)              	-- 10  (o x global)

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

