import os



print("\nMAKING TAGS FROM KEYWORDS")

listpath = "./.lists"

for path in os.listdir(listpath):

	allbiomes = open(listpath + "/" + path).readlines()



	biome_template_types = ["temperate","arid","arctic","desert","ocean"]

	biome_list = {}
	biome_keywords = {}
	biome_simple_list = {}



	base_start = '{"values":['


	for i in biome_template_types:
		biome_list[i] = base_start
		biome_simple_list[i] = []
		biome_keywords[i] = []

	keywords = open("../../../../py/add_mod_biome_compat/modded-keywords.txt").readlines()

	for i in keywords:
		i = i.replace("\n", "")
		pair = i.split("=")
		if len(pair) == 2:
			if pair[1] in biome_keywords.keys():
				biome_keywords[pair[1]].append(pair[0])

	modname = path.replace(".txt","")
	if not os.path.exists(modname):
		os.mkdir(modname)


	for biome in allbiomes:
		biome = biome.replace("\n", "")
		type_select = "ocean"
		for b in biome_keywords:
			for c in biome_keywords[b]:
				if c in biome:
					type_select = b

		biome_list[type_select] += ('\n"' + biome + '",')
		biome_simple_list[type_select].append(biome.replace(modname + ":", ""))




	# printing
	print("\n\033[47m\033[30m" + modname.upper() + "\033[0m")
	for i in biome_simple_list:
		if len(biome_simple_list[i]) > 0:

			
			toprint = ""

			index = 0
			max_index = 8
			for b in biome_simple_list[i]:
				index += 1
				if index < max_index:
					toprint += b + ","
				elif index == max_index + 1:
					toprint += "+" + str(len(biome_simple_list[i]) - (max_index - 1)) + " more."
			toprint = toprint[:-1]
			print(i.title() + " - " + str(len(biome_simple_list[i])) + " entries \033[2m(" + toprint + ")\033[22m")






	for i in biome_list:


		if not biome_list[i] == base_start:
			biome_list[i] = biome_list[i][:-1]
			biome_list[i] += "\n]}"
		
			with open(modname + "/" + i + ".json", "w") as f:
				f.write(biome_list[i])
