--testando coerção de tipos

print(2^"3")			-- 8
print("numero "..1)     -- numero 1

print(2+"2s")			-- erro
print("valor "..true)   -- erro

print(true and false) -- false
print(true and "1")	  -- true
print(true and nil)   -- false

print(true or false)  	-- true
print(false or {1,"a"})	-- true
print(false or nil)   	-- false

