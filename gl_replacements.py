import os
import re

target_folder = 'src/main/java/net/raphimc/thingl/resource'
pattern = re.compile(r'\bGL4[2345]\.gl\b')
replacement = 'de.florianmichael.thingl.GlCommands.get().gl'

for root, _, files in os.walk(target_folder):
    for file in files:
        if file.endswith('.java'):
            file_path = os.path.join(root, file)

            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            new_content = pattern.sub(replacement, content)

            if new_content != content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                print(f'Updated: {file_path}')
