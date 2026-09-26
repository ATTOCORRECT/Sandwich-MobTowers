
import os

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

allbiomes = ""

def addtofile(path):
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



addtofile(dir)


with open(modname + "/list.txt", "w") as f:
	f.write(allbiomes)

print(allbiomes)