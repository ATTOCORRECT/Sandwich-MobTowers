

blocks = open("blocklist.txt").readlines()
template = open("template.json").read()
tags = '{"values": ['

for i in blocks:
	i = i.replace("\n", "")
	json = template.replace("_BLOCK", i)
	tags += '\n"mobtowers:' + i + '",'
	with open("../loot_table/blocks/" + i + ".json", "w") as w:
		w.write(json)

tags = tags[:-1]
tags += "\n]}"

with open("../tags/block/grimstone.json", "w") as w:
	w.write(tags)