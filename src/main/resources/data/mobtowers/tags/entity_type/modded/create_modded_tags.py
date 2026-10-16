

import os


templates_path = "../../../py/add_mod_spawn_prevention_compat/templates/"
java_template_path = "ModTags.java"
java_tag_overwrite_path = "../../../../../../java/net/sandwich/mobtowers/tags/ModTags.java"

no_particles_category = "no particles"


java_tag_register_list = ""
java_summoned_logic_list = ""
java_blocked_logic_list = ""

java_check_mod_boolean_list = ""
java_check_mod_loaded_list = ""

print("\n\n")

for mod in os.listdir(templates_path):


	all_mobs_particles = ""
	all_mobs_no_particles = ""

	read_file = open(templates_path + mod).readlines()

	has_particles = True


	tag_start = '{"values": ['
	tag_end = "]}"

	summoned_mobs = ""
	blocked_mobs = ""

	mod = mod.split(".")[0]


	java_check_mod_boolean_list += "\n	static boolean " + mod.lower() + "_loaded = false;"
	
	java_check_mod_loaded_list += "\n			" + mod.lower() + '_loaded = list.isLoaded("' + mod + '");'
	java_check_mod_loaded_list += '\n			System.out.println("Is ' + mod.lower().replace("_"," ").title() + ' loaded: " + Boolean.toString(' + mod + '_loaded).toUpperCase());'

	for mob in read_file:
		mob = mob.replace("\n", "")

		mob = mob.lower()

		if no_particles_category in mob:
			has_particles = False

		if len(mob) > 1 and not no_particles_category in mob:
			
			if has_particles:
				summoned_mobs += '\n"' + mod + ":" + mob + '",'
				all_mobs_particles += " " + mob + ","
			else:
				blocked_mobs += '\n"' + mod + ":" + mob + '",'
				all_mobs_no_particles += " " + mob + ","

	

	if not os.path.exists(mod):
		os.mkdir(mod)

	

	if len(summoned_mobs) > 0:
		
		summoned_mobs = summoned_mobs[:-1]

		summoned_mobs = tag_start + summoned_mobs + tag_end
		with open(mod + "/" + "tower_summoned.json", "w") as f:
			f.write(summoned_mobs)

		blocked_mobs += '\n"#mobtowers:modded/' + mod + '/tower_summoned",'


		java_tag_register_list += '\n		public static TagKey<EntityType<?>> ' + mod.upper() + '_TOWER_SUMMONED = createTag("modded/' + mod + '/tower_summoned");'
		java_summoned_logic_list += "\n		if (" + mod.lower() + "_loaded) if (entity.is(ModTags.Entities." + mod.upper() + "_TOWER_SUMMONED)) return true;"

	if len(blocked_mobs) > 0:
		blocked_mobs = blocked_mobs[:-1]

		blocked_mobs = tag_start + blocked_mobs + tag_end
		with open(mod + "/" + "tower_blocked.json", "w") as f:
			f.write(blocked_mobs)

		java_tag_register_list += '\n		public static TagKey<EntityType<?>> ' + mod.upper() + '_TOWER_BLOCKED = createTag("modded/' + mod + '/tower_blocked");'
		java_blocked_logic_list += "\n		if (" + mod.lower() + "_loaded) if (entity.is(ModTags.Entities." + mod.upper() + "_TOWER_BLOCKED)) return true;"


	print("\033[47m\033[30m" + mod.upper() + "\033[0m")

	if len(all_mobs_particles) > 0:
		all_mobs_particles = all_mobs_particles[:-1]
		all_mobs_particles = "\033[2mSpawns with particles:\033[0m" + all_mobs_particles
		print(all_mobs_particles)

	if len(all_mobs_no_particles) > 0:
		all_mobs_no_particles = all_mobs_no_particles[:-1]
		all_mobs_no_particles = "\033[2mSpawns without particles:\033[0m" + all_mobs_no_particles
		print(all_mobs_no_particles)

	print("")
		




java_template = open("ModTags.java").read()

java_template = java_template.replace("// MOD LOADED LOGIC GOES HERE", java_check_mod_loaded_list)
java_template = java_template.replace("// MOD LOADED BOOLEANS GO HERE", java_check_mod_boolean_list)
java_template = java_template.replace("// TAG REGISTRIES GO HERE", java_tag_register_list)
java_template = java_template.replace("// TAG BLOCKING LOGIC GOES HERE", java_blocked_logic_list)
java_template = java_template.replace("// TAG SUMMONING LOGIC GOES HERE", java_summoned_logic_list)



confirm_overwrite = input("Do you want to overwrite the ModTags.java logic? You must do this any time you add a new mod. \n(Type yes to confirm)\n>")

if confirm_overwrite.lower() == "yes":
	with open(java_tag_overwrite_path, "w") as f:
		f.write(java_template)
	print("modified " + java_tag_overwrite_path)