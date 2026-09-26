import os

worldgen_path = "../../../../worldgen/"
compat_save_path = worldgen_path + "structure_set/compat/"


structure_set_template = open(worldgen_path + "structure_set/compat_template.txt").read()
structure_entry_template = '\n{"structure": "mobtowers:compat/MOD/mob_tower_BIOME","weight": 1},'

print(structure_set_template)


for mod in os.listdir("./"):
	if not "." in mod:

		all_for_mod = ""

		for b in os.listdir(mod):

			specific_biome = b.replace("all_", "").replace(".json","")
			all_for_mod += structure_entry_template.replace("BIOME",specific_biome).replace("MOD",mod)


			new_structure = open(worldgen_path + "structure/mob_tower_" + specific_biome + ".json").read()

			new_structure = new_structure.replace('"biomes": "#mobtowers:', '"biomes": "#mobtowers:modded/' + mod + "/")

			mod_structure_path = worldgen_path + "structure/compat/" + mod
			if not os.path.exists(mod_structure_path):
				os.mkdir(mod_structure_path)
			with open(mod_structure_path + "/mob_tower_" + specific_biome + ".json", "w") as f:
				f.write(new_structure)

		all_for_mod = all_for_mod[:-1]

		final_save = structure_set_template.replace("INSERT",all_for_mod)
		with open(compat_save_path + mod + ".json", "w") as f:
			f.write(final_save)




