using System.Collections;
using UnityEngine;

/// <summary>
/// Controls target objects behaviour.
/// </summary>
public class MoveToObject : MonoBehaviour
{
    private float _speed = 1.0f;

    private const float _maxDistance = 10;
    private GameObject _playerCamera;

    public void Start()
    {
        _playerCamera = GameObject.FindGameObjectsWithTag("MainCamera")[0];
    }

    /// <summary>
    /// This method is called by the Main Camera when it starts gazing at this GameObject.
    /// </summary>
    public void OnPointerEnter()
    {
        RaycastHit hit;

        if (Physics.Raycast(_playerCamera.transform.position, _playerCamera.transform.forward, out hit, _maxDistance))
        {
            GameObject _gazedAtObject = hit.transform.gameObject;
            transform.position = Vector3.MoveTowards(transform.position, _gazedAtObject.transform.position, _speed * Time.deltaTime);
        }
    }

    /// <summary>
    /// This method is called by the Main Camera when it stops gazing at this GameObject.
    /// </summary>
    public void OnPointerExit()
    {}
}

