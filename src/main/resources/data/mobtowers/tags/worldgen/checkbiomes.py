import os


allbiomes = open("all_overworld.txt").read().split("\n")

allworldgenfiles = os.listdir("biome")

print("Scanning files:")
print(allworldgenfiles)

biomecount = {}

for i in allbiomes:
	biomecount[i] = 0



for i in allworldgenfiles:

	file = open("biome/" + i).read()

	for b in allbiomes:
		if b in file:
			biomecount[b] += 1

errorcount = 0

print("")
for i in biomecount:
	if biomecount[i] > 1:
		errorcount += 1
		print("\033[93m" + i + " has " + str(biomecount[i]) + " mentions!")

for i in biomecount:
	if biomecount[i] == 0:
		errorcount += 1
		print("\033[91m" + i + " is not mentioned!")


if errorcount == 0:
	print("\033[92mAll biomes listed only once each! Good job!")

print("\033[0m")

input("Press enter to close\n")