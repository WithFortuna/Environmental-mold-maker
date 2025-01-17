import pyvista as pv
import os


def perform_mesh_difference(mesh_a_path, mesh_b_path, output_path):
    """
    Load two STL files, perform boolean difference operation (A-B),
    and save the result to a new STL file using PyVista.

    Args:
        mesh_a_path (str): Path to the first STL file (A)
        mesh_b_path (str): Path to the second STL file (B)
        output_path (str): Path where the resulting STL file (C) will be saved

    Returns:
        bool: True if operation was successful, False otherwise
    """
    try:
        mesh_a = pv.read(mesh_a_path)
        mesh_b = pv.read(mesh_b_path)

        print(f"Mesh A points: {mesh_a.n_points}, faces: {mesh_a.n_cells}")
        print(f"Mesh B points: {mesh_b.n_points}, faces: {mesh_b.n_cells}")

        result = mesh_a.boolean_difference(mesh_b)

        result.save(output_path)
        print(f"Successfully saved difference mesh to: {output_path}")

        return True

    except Exception as e:
        print(f"An error occurred: {str(e)}")
        return False


if __name__ == "__main__":
    mesh_a_path = r"/Users/yjk/Developer/playground/pythonPlayground/model2.stl"
    mesh_b_path = r"/Users/yjk/Developer/playground/pythonPlayground/model1.stl"
    output_path = r"/Users/yjk/Developer/playground/pythonPlayground/output.stl"

    success = perform_mesh_difference(mesh_a_path, mesh_b_path, output_path)
    if success:
        print("Operation completed successfully")
    else:
        print("Operation failed")