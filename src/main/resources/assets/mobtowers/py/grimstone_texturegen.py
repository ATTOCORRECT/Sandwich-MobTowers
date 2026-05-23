import os
from PIL import Image



texturesfolder = "../textures/block/"

texture_filename = input("enter texture filename (press ctrl+c to quit)\n").split(".")[0]

path = texturesfolder + texture_filename + ".png"

variantfolder = texturesfolder + "seeping_grimstone/"

mcmeta = open(path + ".mcmeta").read()

if not os.path.exists(variantfolder):
	os.mkdir(variantfolder)


og = Image.open(path)

framecount = int(og.height / og.width)

width = og.width


new = og.copy()


frames = []

for i in range(framecount):

	frames.append(og.crop([0, width * i, width, (i+1) * width]))


for i in range(framecount):
	new = Image.new("RGBA", (og.width, og.height), "#f0f")

	for x in range(framecount):
		s = (x+i) % framecount
		new.paste(frames[s], [0, width * x])
	
	filename = texture_filename + "_" + str(i) + ".png"
	path = variantfolder + filename

	with open(path + ".mcmeta", "w") as f:
		f.write(mcmeta)

	print("saved as " + path)
	new.save(path)