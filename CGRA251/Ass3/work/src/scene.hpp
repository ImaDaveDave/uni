
#pragma once

//std
#include <vector>

// glm
#include <glm/glm.hpp>

// project
#include "cgra/cgra_mesh.hpp"
#include "cgra/cgra_shader.hpp"


// boid class (forward declaration)
class Boid;

class Scene {
private:
	// opengl draw data
	GLuint m_color_shader = 0;
	GLuint m_aabb_shader = 0;
	GLuint m_skymap_shader = 0;
	cgra::gl_mesh m_simple_boid_mesh;
	cgra::gl_mesh m_boid_mesh;
	cgra::gl_mesh m_predator_mesh;
	cgra::gl_mesh m_sphere_mesh;

	// draw status
	bool m_show_aabb = true;
	bool m_show_axis = false;
	bool m_show_skymap = false;

	// scene data
	glm::vec3 m_bound_hsize = glm::vec3(20);
	std::vector<Boid> m_boids;


	//-------------------------------------------------------------
	// [Assignment 3] :
	// Create variables for keeping track of the boid parameters
	// such as min and max speed etc. These paramters can either be
	// public, or private with getter functions.
	//-------------------------------------------------------------

	// YOUR CODE GOES HERE
	// ...
	float minSpeed = 1;
	float maxSpeed = 2;

	//Boid parameters
	float b_neighbourhood = 10;
	float b_avoidance = 0.05;
	float b_alignment = 0.05;
	float b_cohesion = 0.1;

public:

	Scene();

	// functions that load the scene
	void loadCore();
	void loadCompletion();
	void loadChallenge();

	// called every frame, with timestep in seconds
	void update(float timestep);

	// called every frame, with the given projection and view matrix
	void draw(const glm::mat4 &proj, const glm::mat4 &view);

	// called every frame (to fill out a ImGui::TreeNode)
	void renderGUI();

	// returns a const reference to the boids vector
	const std::vector<Boid> & boids() const { return m_boids; }

	// returns the half-size of the bounding box (centered around the origin)
	glm::vec3 bound() const { return m_bound_hsize; }

	// YOUR CODE GOES HERE
	// ...

	//speed getters
	float getMaxSpeed() const { return maxSpeed; }

	//boid parameter getters
	float getNeighbourhood() { return b_neighbourhood; }
	float getAvoid() { return b_avoidance; }
	float getAlign() { return b_alignment; }
	float getCoh() { return b_cohesion; }


	//confinement type booleans
	bool confine_wrap = true;
	bool confine_bounce = false;
	bool confine_force = false;
	float bound_force = 0.1;


};