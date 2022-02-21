using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoveObjectCloser : MonoBehaviour
{
    private float _speed = 1.0f;

    private Renderer _myRenderer;
    private Vector3 _startingPosition;

    /// <summary>
    /// Start is called before the first frame update.
    /// </summary>
    public void Start()
    {
        _startingPosition = transform.localPosition;
    }

    private void MoveCloser(Vector3 playerPosition)
    {
        transform.position = Vector3.MoveTowards(transform.position, playerPosition, _speed * Time.deltaTime);
    }

    private void ResetPosition()
    {
        transform.position = _startingPosition;
    }

    /// <summary>
    /// This method is called by the Main Camera when it starts gazing at this GameObject.
    /// </summary>
    public void OnPointerEnter(Vector3 playerPosition)
    {
        MoveCloser(playerPosition);
    }

    /// <summary>
    /// This method is called by the Main Camera when it stops gazing at this GameObject.
    /// </summary>
    public void OnPointerExit()
    {
        ResetPosition();
    }
}
