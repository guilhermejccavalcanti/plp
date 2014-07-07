
a = "0"

if (a == 0) then
	print("falso")
end

if("abc" == "abc") then
	print("verdadeiro1")
end

if (a ~= 0) then
	print("verdadeiro2")

end

if(2 ~= 0) then
	print("verdadeiro3")
end

if(2 >= 2) then
	print("verdadeiro4")
end

if(3 >= 2) then
	print("verdadeiro5")
end

if("3" >= "2") then
	print("verdadeiro6")
end

if("z" >= "a") then
	print("verdadeiro7")
end

if(2 <= 2) then
	print("verdadeiro8")
end

if(3 <= 5) then
	print("verdadeiro9")
end

if("3" <= "823") then
	print("verdadeiro10")
end

if("b" <= "fff") then
	print("verdadeiro11")
end

if(2 < 34) then
	print("verdadeiro12")
end

if("3" < "5") then
	print("verdadeiro13")
end

if("b" < "fff") then
	print("verdadeiro14")
end

if(23 > 3) then
	print("verdadeiro15")
end

if("16" > "15") then
	print("verdadeiro16")
end

if("zasd" > "fff") then
	print("verdadeiro17")
end


print("2" - "5")

print(#a)

b = {1,"a", true, "123"}

print(#b)

test = {2,3,4,2,a=nil}

print(not test)

print(not false)

print(nil and 10)

--if("d" >= 1) then
--	print("erro")
--end


--a = 1

--print(a[1])
