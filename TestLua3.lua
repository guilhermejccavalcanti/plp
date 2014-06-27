tab = { a3 = "um", b = "dois", c = 3, d = true, 123, "teste", false, tab2 = {1,2,3,"abc", true}, {"efg", "mwn", 53, a=12, b="dso"} }


print("-------------")
print("FOR generico")

tab2 = { a = "um", 123, 333, b = "dois", true}

for k, v in pairs (tab2) do
	print("chave: " .. k)
	print(v)
end
print("-------------")

print("teste" .. 12)

print(tab["a3"])
print(tab[3])
print(tab[a])
print(tab["1"])

print("-------------")
print("Numeros pares de 0 a 30") -- Mostra o texto na tela

for vPar=0, 30, 1 do -- Enquanto vPar for menor ou igual a 100 ele eh incrementado de 1 a 1
    if (vPar % 2 == 0) then -- Se o resto da divisao de vPar por 2 for igual a 0 significa que o valor eh um numero par
        print(vPar) -- Mostra o numero par na tela
    end -- fecha o comando "if"
end -- fecha o comando "for"





