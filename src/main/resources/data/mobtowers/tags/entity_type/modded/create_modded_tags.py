

import os


templates_path = "templates/"
java_template_path = "ModTags.java"
java_tag_overwrite_path = "../../../../../../java/net/sandwich/mobtowers/tags/ModTags.java"

no_particles_category = "no particles"


java_tag_register_list = ""
java_summoned_logic_list = ""
java_blocked_logic_list = ""


for mod in os.listdir(templates_path):
	read_file = open(templates_path + mod).readlines()

	has_particles = True


	tag_start = '{"values": ['
	tag_end = "]}"

	summoned_mobs = ""
	blocked_mobs = ""

	mod = mod.split(".")[0]

	for mob in read_file:
		mob = mob.replace("\n", "")

		mob = mob.lower()

		if no_particles_category in mob:
			has_particles = False

		if len(mob) > 1 and not no_particles_category in mob:

			if has_particles:
				summoned_mobs += '\n"' + mod + ":" + mob + '",'
			else:
				blocked_mobs += '\n"' + mod + ":" + mob + '",'

	

	if not os.path.exists(mod):
		os.mkdir(mod)

	

	if len(summoned_mobs) > 0:
		
		summoned_mobs = summoned_mobs[:-1]

		summoned_mobs = tag_start + summoned_mobs + tag_end
		print(summoned_mobs)

		with open(mod + "/" + "tower_summoned.json", "w") as f:
			f.write(summoned_mobs)

		blocked_mobs += '\n"#mobtowers:modded/' + mod + '/tower_summoned",'


		java_tag_register_list += '\n		public static TagKey<EntityType<?>> ' + mod.upper() + '_TOWER_SUMMONED = createTag("modded/' + mod + '/tower_summoned");'
		java_summoned_logic_list += "\n		if (entity.is(ModTags.Entities." + mod.upper() + "_TOWER_SUMMONED)) return true;"

	if len(blocked_mobs) > 0:
		blocked_mobs = blocked_mobs[:-1]

		blocked_mobs = tag_start + blocked_mobs + tag_end
		print(blocked_mobs)
		with open(mod + "/" + "tower_blocked.json", "w") as f:
			f.write(blocked_mobs)

		java_tag_register_list += '\n		public static TagKey<EntityType<?>> ' + mod.upper() + '_TOWER_BLOCKED = createTag("modded/' + mod + '/tower_blocked");'
		java_blocked_logic_list += "\n		if (entity.is(ModTags.Entities." + mod.upper() + "_TOWER_BLOCKED)) return true;"

	

java_template = open("ModTags.java").read()

java_template = java_template.replace("// TAG REGISTRIES GO HERE", java_tag_register_list)
java_template = java_template.replace("// TAG BLOCKING LOGIC GOES HERE", java_blocked_logic_list)
java_template = java_template.replace("// TAG SUMMONING LOGIC GOES HERE", java_summoned_logic_list)

print(java_template)