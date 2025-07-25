import subprocess

def run(cmd, check=True, capture_output=True):
    return subprocess.run(cmd, check=check, capture_output=capture_output, text=True).stdout.strip()

def remote_exists(name):
    remotes = run(['git', 'remote'])
    return name in remotes.splitlines()

def add_upstream():
    if not remote_exists('upstream'):
        print("Adding 'upstream' remote...")
        run(['git', 'remote', 'add', 'upstream', 'https://github.com/RaphiMC/ThinGL'])
    else:
        print("'upstream' remote already exists.")

def get_current_branch():
    return run(['git', 'rev-parse', '--abbrev-ref', 'HEAD'])

def fetch_upstream():
    print("Fetching 'upstream/main'...")
    run(['git', 'fetch', 'upstream', 'main'])

def merge_upstream_if_main():
    current_branch = get_current_branch()
    if current_branch != 'main':
        print(f"Current branch is '{current_branch}', skipping merge.")
        return

    print("Merging 'upstream/main' into 'main'...")
    try:
        run([
            'git', 'merge', '--no-ff', '--no-commit',
            'upstream/main'
        ])
        run([
            'git', 'commit', '-m', 'Update Upstream (https://github.com/RaphiMC/ThinGL)'
        ])
        print("Merge completed with custom commit message.")
    except subprocess.CalledProcessError as e:
        print("Merge failed or nothing to merge.")
        print(e.stderr)

def main():
    add_upstream()
    fetch_upstream()
    merge_upstream_if_main()

if __name__ == '__main__':
    main()
