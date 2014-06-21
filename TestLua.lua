a	= 2+2*1
t 	= {10,"hello",{1,2}}
M 	= {one=1,two=2,three=3}
i 	= 1

print(a)

print(fact(5))

for i = 1,10 do
	if i < 5 then
		print("lower",i)
	else
		print("upper",i)
	end
end


while i < 5 do
	print("hello",i)
	i = i + 1
end

function fact(n)
   if n == 0 then
      return 1
   else
      return n * fact(n - 1)
   end
end

for i,v in {10,20,30} do 
	print(i,v) 
end
