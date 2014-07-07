print("0" == 0) 		-- false

print("abc" == "abc") 	-- true

print("0" ~= 0) 		-- true

print(2 ~= 0)			-- true

print(2 >= 2)			-- true

print(3 >= 2)			-- true

print("3" >= "2") 		-- true

print("z" >= "a")		-- true

print(2 <= 2) 			-- true	

print(3 <= 5) 			-- true

print("3" <= "823")		-- true

print("b" <= "fff") 	-- true

print(2 < 34) 			-- true

print("3" < "5")		-- true

print("b" < "fff")		-- true

print(23 > 3)			-- true

print("16" > "15") 		-- true

print("zasd" > "fff") 	-- true

print("2" - "5")		-- -3

a = "0"
print(#a)				-- 1

b = {1,"a", true, "123"}
print(#b)				-- 4

test = {2,3,4,2,a=nil}
print(not test)			-- false

print(not false)		-- true

print(nil and 10)	

--print("d" >= 1)

a = 1
print(a[1])
