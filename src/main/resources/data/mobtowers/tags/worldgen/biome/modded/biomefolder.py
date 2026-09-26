import os







biome_template_types = ["temperate","arid","arctic","desert","ocean"]

biome_list = {}
biome_keywords = {}



base_start = '{"values":['


for i in biome_template_types:
	biome_list[i] = base_start
	biome_keywords[i] = []

keywords = open("../modded-keywords.txt").readlines()

for i in keywords:
	i = i.replace("\n", "")
	pair = i.split("=")
	if pair[1] in biome_keywords.keys():
		biome_keywords[pair[1]].append(pair[0])


dir = input("enter directory\n>")

if len(dir) <= 1:
	quit()



def updir(path, amount):

	for i in range(amount):
		path = os.path.dirname(path)
	return path

modname = updir(dir, 2)
modname = os.path.basename(modname).replace(".","_")
if not os.path.exists(modname):
	os.mkdir(modname)




def addtofile(path):
	global allbiomes
	if os.path.exists(path):
		for i in os.listdir(path):
			if (not "." in i):
				addtofile(path + "/" + i)
			else:
				type_select = "ocean"
				for b in biome_keywords:
					for c in biome_keywords[b]:
						if c in i:
							type_select = b

				filename = (path + "/" + i).replace(dir + "/", "")
				filename = modname + ":" + filename
				filename = filename.replace(".json", "")
				biome_list[type_select] += ('\n"' + filename + '",')








addtofile(dir)






for i in biome_list:
	if biome_list[i] != base_start:
		biome_list[i] = biome_list[i][:-1]
		biome_list[i] += "\n]}"

		with open(modname + "/" + i + ".json", "w") as f:
			f.write(biome_list[i])






if not os.path.exists(modname):
	os.mkdir(modname)

# with open(modname + "/all.json", "w") as f:
# 	f.write(allbiomes)