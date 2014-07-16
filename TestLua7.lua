

-- testes return

i = 1

function t()

  	while i < 5 do
		return "teste1"
	end
end


function f()
		if(true) then
			return "teste2"
		else
			print("else2")
		end
		return "return2"
end

function f2()
		if(false) then
			return "teste3"
		else
			return "else3"
		end
		return "return3"
end

print(t())
print(f())
print(f2())

tab2 = { a = "um", 123, 333, b = "dois", true}

for k, v in pairs (tab2) do
	print("chave: " .. k)
	print(v)
	return print("final")
end

print("Numeros pares de 0 a 30")

for vPar=0, 30, 1 do
    if (vPar % 2 == 0) then
        print(vPar)
    end
end










