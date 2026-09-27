
import os



c = input("This will delete data generated from keywords. It will not force you to re-add mods. Type 'delete' to continue.\n> ")

if not c.lower() == 'delete':
	print("Cancelled")
	quit()


def clearfolders(path):
	for i in os.listdir(path):
		if not "." in i and not "list" in i:
			for b in os.listdir(path + i):
				os.remove(path + i + "/" + b)
				print("removed file " + b)
			os.rmdir(path + i)
			print("Cleared " + path + i)

clearfolders("./")

struct_compat_path = "../../../../worldgen/structure/compat/"
set_compat_path = "../../../../worldgen/structure_set/"

if os.path.exists(struct_compat_path):
	clearfolders(struct_compat_path)
	os.rmdir(struct_compat_path)

clearfolders(set_compat_path)