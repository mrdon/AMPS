assert mavenExitCode == 0, "The maven build should not have failed!"

def targetClasses = new File(basedir, 'target/classes')
assert new File(targetClasses, 'foo.js').exists()
assert new File(targetClasses, 'foo-min.js').exists()
assert new File(targetClasses, 'foo.css').exists()
assert new File(targetClasses, 'foo-min.css').exists()
