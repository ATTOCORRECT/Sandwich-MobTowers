import os

allmods = '{"values":['

types = {}






for i in os.listdir("./"):
	if not "." in i:
		for b in os.listdir(i):
			b = b.replace(".json", "")
			if not b in types:
				types[b] = '{"values":['


			types[b] += ('\n"#mobtowers:modded/' + i + "/" + b + '",')

for i in types:
	if len(types[i]) >= 1:
		types[i] = types[i][:-1]
		types[i] += "\n]}"

		with open("all_" + i + ".json", "w") as f:
			f.write(types[i])


input("\n\033[1mPress enter to close\033[0m")