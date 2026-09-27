
import os

dir = input("Enter biomes directory from mod or datapack (data/MOD/worldgen/biome)\n>")


if len(dir) <= 1:
	quit()



def updir(path, amount):

	for i in range(amount):
		path = os.path.dirname(path)
	return path


modname = updir(dir, 2)
modname = os.path.basename(modname).replace(".","_")

allbiomes = ""

entries_recorded = 0

def addtofile(path):
	global entries_recorded
	global allbiomes
	if os.path.exists(path):
		for i in os.listdir(path):
			if (not "." in i):
				addtofile(path + "/" + i)
			else:
				filename = (path + "/" + i).replace(dir + "/", "")
				filename = modname + ":" + filename
				filename = filename.replace(".json", "")

				allbiomes += filename + "\n"
				entries_recorded += 1



addtofile(dir)


with open(".lists/" + modname + ".txt", "w") as f:
	f.write(allbiomes)

print("\nAdded " + str(entries_recorded) + " biomes to list " + modname)