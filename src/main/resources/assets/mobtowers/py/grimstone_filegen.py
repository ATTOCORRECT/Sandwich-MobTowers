
import os

userinput = input("how many frames? (starts at zero; 16 frames will end at 15)\n>")
if userinput == "quit":
	quit()


modelsfolder = "../models/block/"
variantfolder = modelsfolder + "seeping_grimstone/"

frames = int(userinput)

noisevarying = open("grimstone_parent_template.json").read()
noisevariant = open("grimstone_variation_template.json").read()

list = ""

file_name = "seeping_grimstone_active"

if not os.path.exists(variantfolder):
	os.mkdir(variantfolder)


for i in range(frames):
	list += '\n		"mobtowers:block/seeping_grimstone/' + file_name + "_" + str(i) + '",'
	
	path = variantfolder + file_name + "_" + str(i) + ".json"
	print("edited " + path)
	with open(path, "w") as f:
		f.write(noisevariant.replace("NUM", str(i)))

list = list[:-1] + "\n	"

noisevarying = noisevarying.replace('"_VARIANTS"', list)

path = modelsfolder + file_name + ".json"
print("edited " + path)
with open(path, "w") as f:
	f.write(noisevarying)