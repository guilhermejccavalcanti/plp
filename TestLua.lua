-- testes gerais
function fact(n)
   if n == 0 then
      return 1
   else
      return n * fact(n - 1)
   end
end

print(fact(5))

for i = 1,10 do
	if i < 5 then
		print("lower",i)
	else
		print("upper",i)
	end
end

i = 1
while i < 5 do
	print("hello",i)
	i = i + 1
end


