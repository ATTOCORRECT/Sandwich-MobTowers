
import os

dir = input("Enter biomes directory from mod or datapack (data/MOD/worldgen/biome)\n>")


if len(dir) <= 1:
	quit()



def updir(path, amount):

	for i in range(amount):
		path = os.path.dirname(path)
	return path


blacklist = open("../../../../py/add_mod_biome_compat/blacklist-contents.txt").readlines()

modname = updir(dir, 2)
modname = os.path.basename(modname).replace(".","_")

allbiomes = ""

entries_recorded = 0
entries_discarded = 0

print("\n")

def addtofile(path):
	global entries_recorded
	global allbiomes
	global entries_discarded
	if os.path.exists(path):
		for i in os.listdir(path):
			if (not "." in i):
				addtofile(path + "/" + i)
			else:

				contents = open(path + "/" + i).read()



				filename = (path + "/" + i).replace(dir + "/", "")
				filename = modname + ":" + filename
				filename = filename.replace(".json", "")


				has_blacklist = ""
				can_add = True
				for c in blacklist:
					searchfor = c.replace("\n", "")
					if searchfor in contents:
						can_add = False
						has_blacklist = searchfor
						
				if can_add:
					allbiomes += filename + "\n"
					entries_recorded += 1
				else:
					entries_discarded += 1
					print("\033[94m" + filename + " blacklisted (contained '" + has_blacklist + "')\033[0m")





addtofile(dir)

if not os.path.exists(".lists/"):
	os.mkdir(".lists")


with open(".lists/" + modname + ".txt", "w") as f:
	f.write(allbiomes)

print("\nAdded " + str(entries_recorded) + " biomes to list " + modname)


if entries_discarded == 0:
	print("\033[94mNo entries discarded!\033[0m")