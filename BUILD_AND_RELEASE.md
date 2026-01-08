# Building and Releasing Wopper2

This document contains all instructions for building, releasing, and managing versions of the Wopper2 project.

## Version Management

The project version is automatically determined from Git tags:
- **With a Git tag** (e.g., `v1.0.0`): The JAR will be named `wopper2-1.0.0.jar`
- **Without a Git tag**: The JAR will be named `wopper2-dev-SNAPSHOT.jar`

This means you don't need to manually update the version in `build.gradle` - just create a Git tag and the version will be automatically extracted from it.

### How it works

The version is automatically determined in `build.gradle`:

1. **When running on GitHub Actions** (during a release):
   - Uses the `GITHUB_REF_NAME` environment variable
   - Example: Tag `v1.0.0` → Version `1.0.0` → JAR name `wopper2-1.0.0.jar`

2. **When building locally with a Git tag**:
   - Extracts version from `git describe --tags --exact-match`
   - Removes the `v` prefix from tags
   - Example: Tag `v2.5.3` → Version `2.5.3` → JAR name `wopper2-2.5.3.jar`

3. **When building without a Git tag** (development):
   - Falls back to `dev-SNAPSHOT`
   - JAR name: `wopper2-dev-SNAPSHOT.jar`

### Testing locally

To test the version mechanism:

```bash
# Build without a tag (development mode)
./gradlew clean build
ls build/libs/
# Output: wopper2-dev-SNAPSHOT.jar

# Create a tag and build
git tag v1.2.3
./gradlew clean build
ls build/libs/
# Output: wopper2-1.2.3.jar

# Clean up the tag
git tag -d v1.2.3
```

## Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   Git (for version management)

## Building the Project

### Standard Build

To build the project, run the following command in the root directory:

```bash
./gradlew build
```

This will:
- Compile the source code
- Run all tests
- Create a JAR file in `build/libs/`

### Clean Build

To clean previous builds and build from scratch:

```bash
./gradlew clean build
```

### Build without Tests

If you need to skip tests:

```bash
./gradlew build -x test
```

## Running the Application

### Run from Source

To start the application directly from source:

```bash
./gradlew run
```

### Run from JAR

After building, you can run the JAR file:

```bash
java -jar build/libs/wopper2-<version>.jar
```

For example:
```bash
java -jar build/libs/wopper2-1.0.0.jar
```

## Creating a Release

### Method 1: Using GitHub (Recommended)

1. **Commit all changes:**
   ```bash
   git add .
   git commit -m "Prepare release v1.0.0"
   ```

2. **Create and push the version tag:**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

3. **GitHub Actions will automatically:**
   - Build the project
   - Run all tests
   - Create a GitHub release
   - Attach `wopper2-1.0.0.jar` to the release
   - Generate release notes

### Method 2: Manual Release

1. **Create a tag locally:**
   ```bash
   git tag v1.0.0
   ```

2. **Build the project:**
   ```bash
   ./gradlew clean build
   ```

3. **The JAR will be created with the version:**
   ```bash
   ls build/libs/
   # Output: wopper2-1.0.0.jar
   ```

4. **Push the tag (optional):**
   ```bash
   git push origin v1.0.0
   ```

## Re-tagging a Release

If you need to re-tag and retry a release:

```bash
# Delete the local tag
git tag -d v1.0.0

# Delete the remote tag
git push --delete origin v1.0.0

# Create the tag again (after making changes)
git tag v1.0.0

# Push the new tag
git push origin v1.0.0
```

This will trigger the release workflow again with the updated configuration.

## GitHub Actions Workflows

### Build Workflow

**File:** `.github/workflows/build.yml`

**Triggers:**
- Push to `main`, `master`, or `develop` branches
- Pull requests to these branches

**Actions:**
- Sets up JDK 17
- Builds the project
- Runs tests
- Uploads test results and JAR as artifacts

### Release Workflow

**File:** `.github/workflows/release.yml`

**Triggers:**
- Push of tags matching `v*` pattern (e.g., `v1.0.0`)

**Actions:**
- Sets up JDK 17
- Builds the project
- Creates a GitHub release
- Attaches the JAR file to the release
- Generates release notes automatically

**Requirements:**
- The workflow requires `contents: write` permission (already configured)

## Downloading Releases

After a release is created on GitHub, you can:

1. **Visit the Releases page:**
   https://github.com/dedee/wopper2/releases

2. **Download the JAR file:**
   Look for `wopper2-<version>.jar` in the Assets section

3. **Run the downloaded JAR:**
   ```bash
   java -jar wopper2-1.0.0.jar
   ```

## Troubleshooting

### Release workflow fails with 403 error

**Solution:** The workflow needs `contents: write` permission. This is already configured in `.github/workflows/release.yml`:

```yaml
permissions:
  contents: write
```

If you still see errors, check your GitHub repository settings under Settings → Actions → General → Workflow permissions.

### Version shows as "dev-SNAPSHOT" when it shouldn't

**Causes:**
- You don't have a Git tag on the current commit
- The Git tag doesn't match the pattern `v*` (e.g., must be `v1.0.0`, not `1.0.0`)

**Solution:**
```bash
# Check current tags
git tag

# Create a tag if missing
git tag v1.0.0

# Rebuild
./gradlew clean build
```

### JAR file not found after build

**Check:**
```bash
ls -lh build/libs/
```

If empty, the build may have failed. Check the build output for errors.

## Benefits of This Approach

- ✅ **No manual version updates**: Version is derived from Git tags
- ✅ **Consistent versioning**: Same version in Git tag, JAR filename, and GitHub release
- ✅ **Development snapshots**: Clear distinction between releases and development builds
- ✅ **CI/CD friendly**: Works seamlessly with GitHub Actions
- ✅ **Automated releases**: Tag and push - everything else is automatic
- ✅ **Rollback friendly**: Easy to recreate any version by checking out its tag

